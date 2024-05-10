package yamane.hekiraku.model;

import static yamane.hekiraku.util.GsonSupport.*;

import java.util.List;

import com.google.gson.JsonObject;

import yamane.hekiraku.BskyException;

public class LabeledElement extends Identifer {

  private Viewer viewer;
  private List<Label> labels;
  
  public LabeledElement(JsonObject json) throws BskyException {
    super(json);
    this.viewer = json.get("viewer") != null ? new Viewer(json.getAsJsonObject("viewer")) : null;
    this.labels = getList(json.getAsJsonArray("labels"), Label.class);
  }

  public Viewer getViewer() {
    return viewer;
  }
  
  public List<Label> getLabels() {
    return labels;
  }
  
  public static class Label extends Identifer {

    private Integer ver;
    private String src;
    private String val;
    private Boolean neg;
    private String cts;
    private String exp;
    private String sig;
    
    public Label(JsonObject json) {
      super(json);
      this.ver = getInt(json.get("ver"));
      this.src = getStr(json.get("src"));
      this.val = getStr(json.get("val"));
      this.neg = getBoolean(json.get("neg"));
      this.cts = getStr(json.get("cts"));
      this.exp = getStr(json.get("exp"));
      this.sig = getStr(json.get("sig"));
    }

    public Integer getVer() {
      return ver;
    }

    public String getSrc() {
      return src;
    }

    public String getVal() {
      return val;
    }

    public Boolean getNeg() {
      return neg;
    }

    public String getCts() {
      return cts;
    }

    public String getExp() {
      return exp;
    }

    public String getSig() {
      return sig;
    }
  }
}
