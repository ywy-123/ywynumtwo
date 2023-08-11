package pojo;

/**
 * 响应的信息message
 */
public class Message {
    private String statue;//状态
    private String jStr;//Json字符串
    private String description;
    private String value;//随便装

    public Message(){}
    //
    public Message(String statue){
        this.statue=statue;
    }

    public Message(String statue, String jStr) {
        this.statue = statue;
        this.jStr = jStr;
    }

    //返回一个状态
    public String getStatue() {
        return statue;
    }
    //设置状态
    public void setStatue(String statue) {
        this.statue = statue;
    }
    //JsonStr的set和get
    public String getJStr() {
        return jStr;
    }

    public void setJStr(String jStr) {
        this.jStr = jStr;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
