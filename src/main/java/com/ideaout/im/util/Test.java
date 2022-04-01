package com.ideaout.im.util;

import java.util.List;

public class Test {


    /**
     * status : 1
     * body : {"list":{"mobileUserAddressVoList":[{"addressId ":6610,"memberId":2578382,"recipient":"马路大","mobile":"13352638856","provincename":"山东省","cityname":"威海市","areaName":"文登市","street":"龙山路街道","address":"看哭了颇多","isDefault":1,"status":0,"useCache":false}]}}
     */

    public int status;
    public BodyBean body;


    public static class BodyBean {
        /**
         * list : {"mobileUserAddressVoList":[{"addressId ":6610,"memberId":2578382,"recipient":"马路大","mobile":"13352638856","provincename":"山东省","cityname":"威海市","areaName":"文登市","street":"龙山路街道","address":"看哭了颇多","isDefault":1,"status":0,"useCache":false}]}
         */

        public ListBean list;

        public static class ListBean {
            public List<MobileUserAddressVoListBean> mobileUserAddressVoList;

            public static class MobileUserAddressVoListBean {
                /**
                 * addressId  : 6610
                 * memberId : 2578382
                 * recipient : 马路大
                 * mobile : 13352638856
                 * provincename : 山东省
                 * cityname : 威海市
                 * areaName : 文登市
                 * street : 龙山路街道
                 * address : 看哭了颇多
                 * isDefault : 1
                 * status : 0
                 * useCache : false
                 */

                public int addressId;
                public int memberId;
                public String recipient;
                public String mobile;
                public String provincename;
                public String cityname;
                public String areaName;
                public String street;
                public String address;
                public int isDefault;
                public int status;
                public boolean useCache;

            }
        }
    }
}
