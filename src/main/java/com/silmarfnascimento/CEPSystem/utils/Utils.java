package com.silmarfnascimento.CEPSystem.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import com.silmarfnascimento.CEPSystem.model.Client;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {
  public static void copyNonNullProperties(Client source, Client target) {
    System.out.println(source);
    System.out.println(target);
    BeanUtils.copyProperties(source, target, getNullPropertyName(source));
  }

  public static String[] getNullPropertyName(Client source) {
    final BeanWrapper src = new BeanWrapperImpl(source);
    PropertyDescriptor[] pds = src.getPropertyDescriptors();

    Set<String> emptyNames = new HashSet<>();
    System.out.println(emptyNames);
    for (PropertyDescriptor pd : pds) {
      Object srcValue = src.getPropertyValue(pd.getName());
      if (srcValue == null) {
        emptyNames.add(pd.getName());
      }
    }

    String[] result = new String[emptyNames.size()];
    return emptyNames.toArray(result);
  }
}
