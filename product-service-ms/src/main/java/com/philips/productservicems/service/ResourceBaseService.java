package com.philips.productservicems.service;

import com.philips.productservicems.exception.BadRequestException;
import com.philips.productservicems.exception.InvalidSearchCriteriaException;
import com.philips.productservicems.exception.ResourceNotFoundException;
import com.philips.productservicems.exception.ResourceRequestFailedException;
import java.text.ParseException;
import java.util.List;

public interface ResourceBaseService<T> {

  T getById(String id) throws ParseException, ResourceNotFoundException,
      BadRequestException,ResourceRequestFailedException;

  List<T> search() throws ParseException , ResourceNotFoundException,
      ResourceRequestFailedException, InvalidSearchCriteriaException;

  String add(T product) throws ParseException, BadRequestException,
      ResourceRequestFailedException ;

  String delete(String id) throws ParseException ,BadRequestException,
      ResourceRequestFailedException, ResourceNotFoundException;

  T update(String id, T product) throws  ParseException ,BadRequestException,
      ResourceRequestFailedException, ResourceNotFoundException;
}
