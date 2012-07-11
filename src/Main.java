import com.sobey.common.util.DateUtil;
import com.sobey.common.util.R;
import com.sobey.mcss.domain.Broadbandstat;
import org.hibernate.*;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.metadata.ClassMetadata;

import java.util.Calendar;
import java.util.Map;
import java.util.Random;

/**
 * Created by Yanggang.
 * Date: 11-1-7
 * Time: 下午12:04
 * To change this template use File | Settings | File Templates.
 */
public class Main {


    public static void main(final String[] args) throws Exception {
//        Random random = new Random();
//                                  for(int i = 0; i < 10;i++) {
//                                      System.out.println(Math.abs(random.nextInt())%100);
//                                  }

        Calendar compareCalendar = Calendar.getInstance();
                       compareCalendar.set(2011, 10,12,0,0,0);
      while (compareCalendar.get(Calendar.HOUR)<=11 &&compareCalendar.get(Calendar.DATE)==12){

           System.out.println(DateUtil.getSpecificTime(compareCalendar.getTime(),DateUtil._YY_MM_DD_TIME));
            compareCalendar.add(Calendar.HOUR , 1);
      }

    }
}
