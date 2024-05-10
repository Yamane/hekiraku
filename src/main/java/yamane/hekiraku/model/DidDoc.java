package yamane.hekiraku.model;
/*
 * Copyright (c) Yamamoto Yamane
 * Released under the MIT license
 * https://opensource.org/license/mit
 */
import static yamane.hekiraku.util.GsonSupport.*;

import java.io.Serializable;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import yamane.hekiraku.BskyException;

public class DidDoc implements Serializable {

  @SerializedName("@context")
  private List<String> context;
  private String id;
  private List<String> alsoKnownAs;
  private List<VerificationMethod> verificationMethod;
  private List<Service> service;
  
  public DidDoc(JsonObject json) throws BskyException {
    this.context = getList(json.getAsJsonArray("@context"));
    this.id = getStr(json.get("id"));
    this.alsoKnownAs = getList(json.getAsJsonArray("alsoKnownAs"));
    this.verificationMethod = getList(json.getAsJsonArray("verificationMethod"), VerificationMethod.class);
    this.service = getList(json.getAsJsonArray("service"), Service.class);
  }

  public String getId() {
    return id;
  }

  public List<String> getContext() {
    return context;
  }

  public List<String> getAlsoKnownAs() {
    return alsoKnownAs;
  }

  public List<VerificationMethod> getVerificationMethod() {
    return verificationMethod;
  }

  public List<Service> getService() {
    return service;
  }
  
  public String jsonString() {
    return toJson(this);
  }
  
  public static class VerificationMethod implements Serializable {

    private String id;
    private String type;
    private String controller;
    private String publicKeyMultibase;
    
    public VerificationMethod(JsonObject json) {
      this.id = getStr(json.get("id"));
      this.type = getStr(json.get("type"));
      this.controller = getStr(json.get("controller"));
      this.publicKeyMultibase = getStr(json.get("publicKeyMultibase"));
    }

    public String getId() {
      return id;
    }

    public String getType() {
      return type;
    }

    public String getController() {
      return controller;
    }

    public String getPublicKeyMultibase() {
      return publicKeyMultibase;
    }
  }
  
  public static class Service implements Serializable {

    private String id;
    private String type;
    private String serviceEndpoint;
    
    public Service(JsonObject json) {
      this.id = getStr(json.get("id"));
      this.type = getStr(json.get("type"));
      this.serviceEndpoint = getStr(json.get("serviceEndpoint"));
    }

    public String getId() {
      return id;
    }

    public String getType() {
      return type;
    }

    public String getServiceEndpoint() {
      return serviceEndpoint;
    }
  }
}
