package com.markerhub.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.markerhub.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 李崇钧
 * @since 2021-07-08
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<Long> getNavMenuIds(Long userId);

    @Override
    List<Map<String, Object>> selectMaps(Wrapper<SysUser> queryWrapper);

    List<SysUser> listByMenuId(Long menuId);
}
