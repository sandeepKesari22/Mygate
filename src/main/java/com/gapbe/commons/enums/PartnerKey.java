package com.gapbe.commons.enums;

public enum PartnerKey {
  FLIPKART("Ib8Un76ZNK26rH0ymnkky84AkECvS3082Oaae87t"),
  GROFERS("DFsD6GCozW5sb81ioWIZU6Ts2wsbeIqwkBPN7"),
  SWIGGY("tmInNG2jaM9SW1vqFKHSa9l6e0bpuVO01z12CESAd");

  String key;

  PartnerKey(String key) {
    this.key = key;
  }


  public String getKey(){
    return this.key;
  }
}
