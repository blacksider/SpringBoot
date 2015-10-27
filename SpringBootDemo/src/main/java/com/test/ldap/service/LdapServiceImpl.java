package com.test.ldap.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import javax.naming.directory.SearchControls;

import org.dom4j.IllegalAddException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.test.common.Constants.LdapGroupType;
import com.test.dao.LdapGroupRepository;
import com.test.ldap.pojo.Group;
import com.test.ldap.pojo.Person;
import com.test.ldap.repo.CustomLdapRepository;
import com.test.model.LdapGroup;
import com.test.model.LdapGroupServerInfo;

@Service
public class LdapServiceImpl implements LdapService {
	@Autowired
	private LdapGroupRepository groupRepository;

	@Autowired
	private CustomLdapRepository ldapRepository;

	@Autowired
	private TransactionTemplate template;

	@Override
	public Observable<Long> saveNewServerGroup(LdapGroup ldapGroup) {
		Observable<Long> ob = Observable.create(observer -> {
			try {
				ldapGroup.setGroupType(LdapGroupType.SERVER);

				LdapGroupServerInfo serverInfo = ldapGroup.getServerInfo();
				byte[] ps = Base64.encode(serverInfo.getPassword().getBytes());
				serverInfo.setPassword(new String(ps));
				ldapGroup.setServerInfo(serverInfo);

				long id = groupRepository.save(ldapGroup).getId();
				observer.onNext(id);
				observer.onCompleted();
			} catch (Exception e) {
				e.printStackTrace();
				observer.onError(e);
			}
		});
		return ob.subscribeOn(Schedulers.io());
	}

	@Override
	public Observable<LdapGroupServerInfo> getGroupServerInfo(long ldapGroupID) {
		Observable<LdapGroupServerInfo> ob = Observable.create(observer -> {
			try {
				LdapGroupServerInfo serverInfo = template.execute(status -> {
					LdapGroup group = groupRepository.findOne(ldapGroupID);
					if (group == null) {
						throw new IllegalAddException(
								"Can not found group by id: " + ldapGroupID);
					}
					return group.getServerInfo();
				});
				observer.onNext(serverInfo);
				observer.onCompleted();
			} catch (Exception e) {
				e.printStackTrace();
				observer.onError(e);
			}
		});
		return ob.subscribeOn(Schedulers.io());
	}

	// by given server to import all its group info from ldap and store it into
	// our database(a group tree)
	@Override
	public Observable<LdapGroup> loadGroupOfServer(
			LdapGroupServerInfo serverInfo) {
		Observable<LdapGroup> ob = Observable.create(observer -> {
			try {
				String serverBase = serverInfo.getBase();

				// search current server's groups
				List<Group> groups = ldapRepository.findGroupsByBase(
						serverInfo, serverBase, SearchControls.SUBTREE_SCOPE);

				if (groups == null || groups.isEmpty()) {
					throw new IllegalArgumentException("No person in group"
							+ serverBase);
				}
				// save base info and set serverdn current server dn
				List<LdapGroup> saveList = new ArrayList<>();
				for (Iterator<Group> iterator = groups.iterator(); iterator
						.hasNext();) {
					Group group = iterator.next();
					LdapGroup ldapGroup = new LdapGroup();
					ldapGroup.setDn(group.getDn().toString());
					ldapGroup.setGroupName(group.getGroupName());
					ldapGroup.setDescription(group.getDescription());
					ldapGroup.setGroupType(LdapGroupType.GROUP);
					// set to group by one server
					ldapGroup.setServerDN(serverBase);
					saveList.add(ldapGroup);
				}
				// get all groups from db which server dn is serverinfo's
				// base attr and check parent group
				List<LdapGroup> allDBGroups = groupRepository.save(saveList);
				if (allDBGroups == null || allDBGroups.isEmpty()) {
					throw new IllegalStateException("DB state error");
				}
				saveList.clear();
				for (Iterator<LdapGroup> iterator = allDBGroups.iterator(); iterator
						.hasNext();) {
					LdapGroup ldapGroup = iterator.next();
					String dn = ldapGroup.getDn();
					LdapGroup parent = groupRepository.findByDn(
							dn.substring(dn.indexOf(",") + 1)).get(0);
					if (parent == null) {
						throw new IllegalArgumentException(
								"Can not found ldap group in db");
					}
					ldapGroup.setParentGroup(parent);
					saveList.add(ldapGroup);
				}
				allDBGroups = groupRepository.save(saveList);
				if (allDBGroups == null || allDBGroups.isEmpty()) {
					throw new IllegalStateException("DB state error");
				}
				// if saved, get the group tree of current server
				LdapGroup rs = template.execute(status -> {
					LdapGroup rootGroup = groupRepository.findByDn(serverBase)
							.get(0);
					goThroughAllGroups(rootGroup, (group) -> {
						Assert.isTrue(group.getId() > 0,
								"group id should be greater than 0");
					});
					return rootGroup;
				});
				observer.onNext(rs);
				observer.onCompleted();
			} catch (Exception e) {
				e.printStackTrace();
				observer.onError(e);
			}
		});
		return ob.subscribeOn(Schedulers.io());
	}

	@Override
	public Observable<LdapGroup> getGroupTree(long groupID) {
		Observable<LdapGroup> ob = Observable.create(observer -> {
			try {
				template.execute(status -> {
					LdapGroup rootGroup = groupRepository.findOne(groupID);
					goThroughAllGroups(rootGroup, (group) -> {
						Assert.isTrue(group.getId() > 0,
								"group id should be greater than 0");
					});
					observer.onNext(rootGroup);
					observer.onCompleted();
					return status;
				});
			} catch (Exception e) {
				e.printStackTrace();
				observer.onError(e);
			}
		});
		return ob.subscribeOn(Schedulers.io());
	}

	// get person in current group
	@Override
	public Observable<List<Person>> getGroupPerson(long groupID) {
		Observable<List<Person>> ob = Observable
				.create(observer -> {
					try {
						LdapGroup group = groupRepository.findOne(groupID);
						if (group == null) {
							throw new IllegalArgumentException(
									"No such group with id: " + groupID);
						}
						LdapGroup server = null;
						if (group.getGroupType() == LdapGroupType.GROUP) {
							server = groupRepository.findByDn(
									group.getServerDN()).get(0);
						} else if (group.getGroupType() == LdapGroupType.SERVER) {
							server = group;
						} else {
							throw new IllegalArgumentException(
									"No such group type!");
						}
						LdapGroupServerInfo serverInfo = server.getServerInfo();
						List<Person> persons = ldapRepository
								.findPersonsByBase(serverInfo, group.getDn(),
										SearchControls.SUBTREE_SCOPE);

						observer.onNext(persons);
						observer.onCompleted();
					} catch (Exception e) {
						e.printStackTrace();
						observer.onError(e);
					}
				});
		return ob.subscribeOn(Schedulers.io());
	}

	// TODO 根据Email查询人，遍历所有组还是单个组
	@Override
	public Observable<Person> getPersonByEmail() {
		Observable<Person> ob = Observable.create(observer -> {
			try {
				observer.onNext(null);
				observer.onCompleted();
			} catch (Exception e) {
				e.printStackTrace();
				observer.onError(e);
			}
		});
		return ob.subscribeOn(Schedulers.io());
	}

	// get group and person with no db data, use ldap interface to get it
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Observable<LdapGroup> loadGroupOfServerNoDB(
			LdapGroupServerInfo serverInfo) {
		Observable<LdapGroup> ob = Observable.create(observer -> {
			try {
				String serverBase = serverInfo.getBase();

				// rootGroup为服务器当前组
				LdapGroup rootGroup = new LdapGroup();
				rootGroup.setDn(serverBase);
				rootGroup.setGroupName(serverBase);
				rootGroup.setServerInfo(serverInfo);

				// search current server's groups
				List<Group> groups = ldapRepository.findGroupsByBase(
						serverInfo, serverBase, SearchControls.SUBTREE_SCOPE);

				if (groups == null || groups.isEmpty()) {
					throw new IllegalArgumentException("No person in group"
							+ serverBase);
				}

				int groupSize = groups.size();
				// 只查到一个子组
				if (groupSize == 1) {
					Group rsGroup = groups.get(groupSize - 1);
					if (getCharacterCount(serverBase, ',') == getCharacterCount(
							rsGroup.getDn().toString(), ',')) {
						throw new IllegalArgumentException("No groups in group"
								+ serverBase);
					}
					List<LdapGroup> subs = new ArrayList<LdapGroup>();
					subs.add(toLdapGroup(rsGroup));
					rootGroup.setSubGroups(subs);
					observer.onNext(rootGroup);
					observer.onCompleted();
				} else {
					Comparator myComparator = new LdapGroupComparator();
					Collections.sort(groups, myComparator);
					// 查询出树的层级
					int size = getCharacterCount(groups.get(groups.size() - 1)
							.getDn().toString(), ',');
					// 子组只有一层
					if (size == 1) {
						List<LdapGroup> subs = new ArrayList<LdapGroup>();
						for (Group group : groups) {
							subs.add(toLdapGroup(group));
						}
						rootGroup.setSubGroups(subs);
						observer.onNext(rootGroup);
						observer.onCompleted();
					} else {
						// 子组有多层
						// 所有层的集合
						List<List<LdapGroup>> allLevelLdapList = new ArrayList<>(
								size);
						// 当前层的集合
						List<LdapGroup> eachLevelLdapList = new ArrayList<>();
						// 根组','个数
						int rootCharCount = getCharacterCount(
								rootGroup.getDn(), ',');
						// 当前层为根组层
						eachLevelLdapList.add(rootGroup);
						// 所有层1的位置为根层
						allLevelLdapList.add(eachLevelLdapList);
						// 将组分层分好
						for (int i = 1; i < size; i++) {
							// 清空当前层数据
							eachLevelLdapList = new ArrayList<LdapGroup>();
							// 每一层','个数
							int currentCharCount = rootCharCount + i;
							// 取出当前层
							for (Group group : groups) {
								if (getCharacterCount(group.getDn().toString(),
										',') == currentCharCount) {
									eachLevelLdapList.add(toLdapGroup(group));
								}
							}
							// 添加到所有层
							allLevelLdapList.add(eachLevelLdapList);
						}
						// 逆向遍历层组
						for (int i = size - 1; i > 0; i--) {
							// 当i=1时代表是第二层，只需要将第二层设到根组上
							if (i == 1) {
								rootGroup.setSubGroups(allLevelLdapList.get(i));
							} else {
								// 当前层的集合
								List<LdapGroup> li = allLevelLdapList.get(i);
								// 父层的集合
								List<LdapGroup> liParent = allLevelLdapList
										.get(i - 1);
								for (LdapGroup group : li) {
									// 将当前层加到父层的subgroups中
									String currentDN = group.getDn();
									String parentDN = currentDN
											.substring(currentDN.indexOf(',') + 1);
									for (Iterator<LdapGroup> iterator = liParent
											.iterator(); iterator.hasNext();) {
										LdapGroup parent = iterator.next();
										if (parentDN.equals(parent.getDn())) {
											List<LdapGroup> subGroups = parent
													.getSubGroups();
											if (subGroups == null) {
												subGroups = new ArrayList<LdapGroup>();
											}
											subGroups.add(group);
											parent.setSubGroups(subGroups);
										}
									}
								}
							}
						}
						observer.onNext(rootGroup);
						observer.onCompleted();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				observer.onError(e);
			}
		});
		return ob.subscribeOn(Schedulers.io());
	}

	// 验证用户信息
	@Override
	public Observable<Boolean> authenticate(LdapGroupServerInfo serverInfo,
			String userDN, String credentials) {
		Observable<Boolean> ob = Observable.create(observer -> {
			try {
				observer.onNext(ldapRepository.authenticateUser(serverInfo,
						userDN, credentials));
				observer.onCompleted();
			} catch (Exception e) {
				e.printStackTrace();
				observer.onError(e);
			}
		});

		return ob.subscribeOn(Schedulers.io());
	}

	// go through all group and subgroups
	private void goThroughAllGroups(LdapGroup group,
			Consumer<LdapGroup> walkFunc) {
		walkFunc.accept(group);
		List<LdapGroup> subGroups = group.getSubGroups();
		if (subGroups != null && subGroups.size() > 0) {
			for (LdapGroup subGroup : subGroups) {
				goThroughAllGroups(subGroup, walkFunc);
			}
		}
	}

	private int getCharacterCount(String desStr, char countChar) {
		int count = 0;
		for (int i = 0; i < desStr.length(); i++) {
			if (desStr.charAt(i) == countChar) {
				count++;
			}
		}
		return count;
	}

	private LdapGroup toLdapGroup(Group group) {
		LdapGroup ldapGroup = new LdapGroup();
		ldapGroup.setDn(group.getDn().toString());
		ldapGroup.setGroupName(group.getGroupName());
		ldapGroup.setDescription(group.getDescription());
		return ldapGroup;
	}

	private final class LdapGroupComparator implements Comparator<Group> {
		@Override
		public int compare(Group group1, Group group2) {
			String group1DN = group1.getDn().toString();
			String group2DN = group2.getDn().toString();

			int dn1Count = getCharacterCount(group1DN, ',');
			int dn2Count = getCharacterCount(group2DN, ',');
			if (dn1Count == dn2Count) {
				return group1DN.charAt(0) - group2DN.charAt(0);
			}
			return dn1Count - dn2Count;
		}
	}

}
