package com.sobey.mcss.action;

import com.sobey.mcss.domain.Hourstatitem;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Yanggang
 * Date: 11-12-7
 * Time: 上午11:08
 * To change this template use File | Settings | File Templates.
 */
@WebService
//@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface McssService {

   public List<Hourstatitem> demo();
   public String getMediaStorageUsage(String host, String password, String volumePath);
}
