package piyopiyo.py.skeltons;

import java.util.List;

public class BinarySkelton extends Skelton {
    private final List<Skelton> e0;
    private final List<Skelton> e1;

    public BinarySkelton(List<Skelton> e0, List<Skelton> e1) {
        this.e0 = e0;
        this.e1 = e1;
    }
}
