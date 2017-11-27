package masegi.sho.mytimetable.domain.value;

/**
 * Created by masegi on 2017/09/27.
 */

public class Library {

    private String name;
    private String copyRight;
    private License license;

    public Library(String name, String copyRight, License license) {

        this.name = name;
        this.copyRight = copyRight;
        this.license = license;
    }

    public String getName() { return this.name; }
    public String getCopyRight() { return this.copyRight; }
    public License getLicense() { return this.license; }

}

