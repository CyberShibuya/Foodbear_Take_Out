package com.bear.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bear.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
