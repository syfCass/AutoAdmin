package cn.songhaiqing.tool.service;

import cn.songhaiqing.tool.entity.SysRole;
import cn.songhaiqing.tool.base.BaseQuery;
import cn.songhaiqing.tool.base.BaseResponseList;
import cn.songhaiqing.tool.commons.DateUtils;
import cn.songhaiqing.tool.model.SysRoleViewModel;
import cn.songhaiqing.tool.repository.SysRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    @Autowired
    private SysUserRoleService sysUserRoleService;


    @Transactional
    @Override
    public void insert(SysRoleViewModel model, Long[] menuIds) {
        SysRole sysRole = new SysRole();
        sysRole.setId(model.getId());
        sysRole.setName(model.getName());
        sysRole.setDes(model.getDes());
        sysRoleRepository.save(sysRole);
        sysRolePermissionService.addPermission(sysRole.getId(), menuIds);
    }

    @Transactional
    @Override
    public void update(SysRoleViewModel model, Long[] menuIds) {
        SysRole sysRole = sysRoleRepository.findOne(model.getId());
        sysRole.setId(model.getId());
        sysRole.setName(model.getName());
        sysRole.setDes(model.getDes());
        sysRole.setUpdateTime(new Date());
        sysRoleRepository.save(sysRole);
        List<Long> ids = new ArrayList<>();
        for (Long menuId : menuIds) {
            ids.add(menuId);
        }
        sysRolePermissionService.updatePermission(model.getId(), ids);
    }

    @Override
    public SysRoleViewModel getSysRoleDetail(long id) {
        SysRole sysRole = sysRoleRepository.findOne(id);
        SysRoleViewModel model = new SysRoleViewModel();
        if(sysRole == null) {
            return model;
        }
        model.setId(sysRole.getId());
        model.setName(sysRole.getName());
        model.setDes(sysRole.getDes());
        model.setUpdateTime(DateUtils.dateToLongString(sysRole.getUpdateTime()));
        model.setCreateTime(DateUtils.dateToLongString(sysRole.getCreateTime()));
        return model;
    }

    @Override
    public BaseResponseList<SysRoleViewModel> getSysRole(BaseQuery query) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"id"));
        Sort sort = new Sort(orders);
        Page<SysRole> page  =  sysRoleRepository.findAll(new Specification<SysRole>(){
            @Override
            public Predicate toPredicate(Root<SysRole> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicate = new ArrayList<>();
                // TODO 示例
                /**
                 if (query.getType() != null) {
                 predicate.add(cb.equal(r.get("type").as(Integer.class), query.getType()));
                 }
                 */
                Predicate[] pre = new Predicate[predicate.size()];
                return criteriaQuery.where(predicate.toArray(pre)).getRestriction();
            }
        },new PageRequest(query.getPage() - 1, query.getLimit(),sort));
        BaseResponseList<SysRoleViewModel> result = new BaseResponseList<>();
        result.setCount(page.getTotalElements());
        List<SysRoleViewModel> models = new ArrayList<>();
        for (SysRole sysRole : page.getContent()) {
            SysRoleViewModel model = new SysRoleViewModel();
            model.setId(sysRole.getId());
            model.setName(sysRole.getName());
            model.setDes(sysRole.getDes());
            model.setUpdateTime(DateUtils.dateToLongString(sysRole.getUpdateTime()));
            model.setCreateTime(DateUtils.dateToLongString(sysRole.getCreateTime()));
            models.add(model);
        }

        result.setData(models);
        return result;
    }

    @Override
    public List<SysRoleViewModel> getAllRoles(Long userId) {
        List<SysRoleViewModel> models = new ArrayList<>();
        List<SysRole> sysRoles = sysRoleRepository.findAll();
        if(CollectionUtils.isEmpty(sysRoles)) {
            return models;
        }
        List<Long> roleIds = null;
        if(userId != null) {
            roleIds = sysUserRoleService.getRoleIdsByUser(userId);
        }
        for (SysRole sysRole : sysRoles) {
            SysRoleViewModel model = new SysRoleViewModel();
            model.setId(sysRole.getId());
            model.setName(sysRole.getName());
            model.setDes(sysRole.getDes());
            if(roleIds != null && roleIds.contains(sysRole.getId())) {
                model.setChecked(true);
            }else{
                model.setChecked(false);
            }
            models.add(model);
        }
        return models;
    }
}
