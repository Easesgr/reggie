package com.anyi.reggie.service.impl;

import com.anyi.reggie.entity.Employee;
import com.anyi.reggie.mapper.EmployeeMapper;
import com.anyi.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author anyi
 * @since 2022-05-24
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
