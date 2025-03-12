package kr.kosa.bowl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.kosa.bowl.storage.SnackFileIO;
import kr.kosa.bowl.util.AbstractFileIO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Snack implements Serializable {

    private static final long serialVersionUID = 548165165343L;
    private static Snack instance;
    private String snackName;
    private int snackPrice;
    private int snackCnt;
    private transient  Map<String, Snack> snackMap;
    private static transient AbstractFileIO<Map<String, Snack>> fileIO;

    public Snack(String snackName, int snackPrice, int snackCnt) {
        this.snackName = snackName;
        this.snackPrice = snackPrice;
        this.snackCnt = snackCnt;
    }

    private Snack() {
        fileIO = new SnackFileIO();
        Map<String, Snack> readSnackMap = fileIO.loadFile();
        this.snackMap = readSnackMap;
    }

    public static Snack getInstance() {
        if (instance == null) {
            instance = new Snack();
        }
        return instance;
    }
}
