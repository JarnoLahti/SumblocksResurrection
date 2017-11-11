package fi.jarno.sumblocks.resurrection.Resources;

/**
 * Created by Jarno on 26-Oct-17.
 */

public class MergeMetadata {
    public int x;
    public int y;
    public boolean mergeBlock;

    public MergeMetadata(int x, int y){
        this.x = x;
        this.y = y;
    }

    public boolean equals(int x, int y){
        return this.x == x && this.y == y;
    }
}
