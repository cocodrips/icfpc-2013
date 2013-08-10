package piyopiyo.py.skeltons;

import java.util.List;

public class FoldSkelton extends Skelton {
    private final List<Skelton> e0;
    private final List<Skelton> e1;
    private final List<Skelton> e2;

    public FoldSkelton(List<Skelton> e0, List<Skelton> e1, List<Skelton> e2) {
        this.e0 = e0;
        this.e1 = e1;
        this.e2 = e2;
    }
}
