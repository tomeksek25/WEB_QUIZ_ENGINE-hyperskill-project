package engine;



import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;
    private String text;
    private String[] options;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int[] answer;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String user;

    public String getTitle() {
        return this.title;
    }

    public String getText() {
        return this.text;
    }

    public String[] getOptions() {
        return this.options;
    }

    public int[] getAnswer() {
        if (answer == null) {
            return new int[0];
        }
        Arrays.sort(this.answer);
        return this.answer;
    }

    public int getId() {
        return this.id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public void setAnswer(int[] answer) {
        this.answer = answer;
    }

    public void setId(int id) {
        this.id = id;
    }

}
