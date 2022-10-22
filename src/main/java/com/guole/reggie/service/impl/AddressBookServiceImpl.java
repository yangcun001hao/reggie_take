package com.guole.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guole.reggie.entity.AddressBook;
import com.guole.reggie.mapper.AddressBookMapper;
import com.guole.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author 郭乐
 * @date 2022/10/21
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
