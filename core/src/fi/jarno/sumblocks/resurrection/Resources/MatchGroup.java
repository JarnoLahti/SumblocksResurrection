package fi.jarno.sumblocks.resurrection.Resources;

import java.util.ArrayList;

/**
 * Created by Jarno on 09-Nov-17.
 */

public class MatchGroup {
    private int _color;
    private ArrayList<MergeMetadata> _mergeMetadata;

    public MatchGroup(int color){
        _color = color;
        _mergeMetadata = new ArrayList();
    }

    public boolean overlaps(int color, int x, int y){
        if(_color == color){
            for (MergeMetadata mmd:_mergeMetadata) {
                if(mmd.equals(x, y)){
                    return true;
                }
            }
        }

        return false;
    }

    public ArrayList<MergeMetadata> getMetadata(){
        return _mergeMetadata;
    }

    public int getColor(){
        return _color;
    }

    public void addMetadata(MergeMetadata mergeMetaData){
        _mergeMetadata.add(mergeMetaData);
    }

    public void addMergeGroup(MatchGroup group){
        for (MergeMetadata mmd : group.getMetadata()) {
            _mergeMetadata.add(mmd);
        }
    }

    public MergeMetadata getMergeBlock(){
        for (MergeMetadata md : _mergeMetadata) {
            if(md.mergeBlock){
                return md;
            }
        }
        return null;
    }

    public ArrayList<MergeMetadata> getNonMergeBlocks(){
        ArrayList<MergeMetadata> data = new ArrayList();
        for (MergeMetadata md : _mergeMetadata) {
            if(!md.mergeBlock){
                data.add(md);
            }
        }
        return data;
    }

    public void setMergeBlock(int x, int y){
        if(getMergeBlock() != null){
            return;
        }

        for (MergeMetadata md : _mergeMetadata) {
            if(md.x == x && md.y == y){
                md.mergeBlock = true;
            }
        }
    }
}
