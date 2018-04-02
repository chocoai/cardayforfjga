package com.cmdt.carrental.common.service;

import org.apache.shiro.authz.permission.WildcardPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmdt.carrental.common.dao.ResourceDao;
import com.cmdt.carrental.common.entity.Resource;
import com.cmdt.carrental.common.util.TreeNode;
import com.cmdt.carrental.common.util.TreeNodeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service

public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceDao resourceDao;

    public Resource createResource(Resource resource) {
        return resourceDao.createResource(resource);
    }

    public Resource updateResource(Resource resource) {
        return resourceDao.updateResource(resource);
    }

    public void deleteResource(Long resourceId) {
        resourceDao.deleteResource(resourceId);
    }

    public Resource findOne(Long resourceId) {
        return resourceDao.findOne(resourceId);
    }

    public List<Resource> findByResourceId(Long resourceId) {
        return resourceDao.findByResourceId(resourceId);
    }
    
    public List<Resource> findByResourceIds(String resourceIds) {
        return resourceDao.findByResourceIds(resourceIds);
    }
    
    public List<Resource> findAll() {
        return resourceDao.findAll();
    }

	@Override
	public List<String> list() {
		return resourceDao.list();
	}

	public TreeNode formatList2Tree(List<Resource> list) {
		if (list == null || list.size() == 0) {
			return null;
		}

		// tree root id
		Long rootId = list.get(0).getId();

		Map<Long, TreeNode> retMap = new HashMap<Long, TreeNode>();
		for (Resource nativeNode : list) {
			Long id = nativeNode.getId();
			Resource nativeTreeNode = nativeNode;
			TreeNode treeNode = new TreeNode();
			treeNode.setId(id);
			treeNode.setText(nativeTreeNode.getName());
			retMap.put(id, treeNode);

			String[] parentIdsArr = nativeTreeNode.getParentIds().split(",");
			int parentIdsLength = parentIdsArr.length;
			if (parentIdsLength == 1) {
				continue;
			}

			Long directParentId = Long.valueOf(parentIdsArr[parentIdsLength - 1]);

			TreeNode parentNode = retMap.get(directParentId);
			if (parentNode != null) {
				if (parentNode.getChildren() == null) {
					List<TreeNode> children = new ArrayList<TreeNode>();
					children.add(treeNode);
					parentNode.setChildren(children);
				} else {
					parentNode.getChildren().add(treeNode);
				}
				parentNode.setExpanded(Boolean.TRUE);
				parentNode.setLeaf(Boolean.FALSE);
			}
		}

		TreeNode rootTreeNode = retMap.get(rootId);
		return rootTreeNode;
	}
    
	public TreeNodeView formatList2TreeView(List<Resource> list) {
		if (list == null || list.size() == 0) {
			return null;
		}

		// tree root id
		Long rootId = list.get(0).getId();

		Map<Long, TreeNodeView> retMap = new HashMap<Long, TreeNodeView>();
		for (Resource nativeNode : list) {
			Long id = nativeNode.getId();
			Resource nativeTreeNode = nativeNode;
			TreeNodeView treeNodeView = new TreeNodeView();
			treeNodeView.setId(id);
			treeNodeView.setText(nativeTreeNode.getName());
			retMap.put(id, treeNodeView);

			String[] parentIdsArr = nativeTreeNode.getParentIds().split(",");
			int parentIdsLength = parentIdsArr.length;
			if (parentIdsLength == 1) {
				continue;
			}

			Long directParentId = Long.valueOf(parentIdsArr[parentIdsLength - 1]);

			TreeNodeView parentNode = retMap.get(directParentId);
			if (parentNode != null) {
				if (parentNode.getChildren() == null) {
					List<TreeNodeView> children = new ArrayList<TreeNodeView>();
					children.add(treeNodeView);
					parentNode.setChildren(children);
				} else {
					parentNode.getChildren().add(treeNodeView);
				}
				parentNode.setExpanded(Boolean.TRUE);
				parentNode.setLeaf(Boolean.FALSE);
			}
		}

		TreeNodeView rootTreeNode = retMap.get(rootId);
		return rootTreeNode;
	}
	
    public Set<String> findPermissions(Set<Long> resourceIds) {
        Set<String> permissions = new HashSet<String>();
        for(Long resourceId : resourceIds) {
            Resource resource = findOne(resourceId);
            if(resource != null && !StringUtils.isEmpty(resource.getPermission())) {
                permissions.add(resource.getPermission());
            }
        }
        return permissions;
    }
    
    public List<Resource> findButtons(Set<String> permissions) {
        List<Resource> allResources = findAll();
        List<Resource> menus = new ArrayList<Resource>();
        for(Resource resource : allResources) {
            if(resource.isRootNode()) {
                continue;
            }
//         if(resource.getType() != Resource.ResourceType.button) {
            if(!resource.getType().equals("button")) {
                continue;
            }
            if(!hasPermission(permissions, resource)) {
                continue;
            }
            menus.add(resource);
        }
        return menus;
    }

    public List<Resource> findMenus(Set<String> permissions) {
        List<Resource> allResources = findAll();
        List<Resource> menus = new ArrayList<Resource>();
        for(Resource resource : allResources) {
            if(resource.isRootNode()) {
                continue;
            }
//         if(resource.getType() != Resource.ResourceType.menu) {
            if(!resource.getType().equals("menu")) {
                continue;
            }
            if(!hasPermission(permissions, resource)) {
                continue;
            }
            menus.add(resource);
        }
        return menus;
    }

    private boolean hasPermission(Set<String> permissions, Resource resource) {
        if(StringUtils.isEmpty(resource.getPermission())) {
            return true;
        }
        if(permissions == null || permissions.isEmpty()){
        	return true;
        }
        for(String permission : permissions) {
            WildcardPermission p1 = new WildcardPermission(permission);
            WildcardPermission p2 = new WildcardPermission(resource.getPermission());
            if(p1.implies(p2) || p2.implies(p1)) {
                return true;
            }
        }
        return false;
    }

	@Override
	public List<Resource> findResourcesByRoleId(Long roleId) {
		// TODO Auto-generated method stub
		return resourceDao.findResourcesByRoleId(roleId);
	}
}
