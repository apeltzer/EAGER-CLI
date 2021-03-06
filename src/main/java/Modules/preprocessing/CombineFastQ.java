package Modules.preprocessing;

import IO.Communicator;
import Modules.AModule;
import com.google.common.io.Files;

import java.util.ArrayList;

/**
 * Created by peltzer on 26.04.17.
 */
public class CombineFastQ extends AModule{


    public CombineFastQ(Communicator c) {
        super(c);
    }

    @Override
    public void setParameters() {
        String output_stem = Files.getNameWithoutExtension(this.inputfile.get(0));
        this.outputfile = new ArrayList<String>();
        this.parameters = getDefaultParameters();
        this.outputfile.add(getOutputfolder() + output_stem + ".combined.fq.gz");

    }

    private String[] getDefaultParameters() {
        String output_stem = Files.getNameWithoutExtension(this.inputfile.get(0));
        //stem would be WGS42_S0_L001_R1_001.fastq.gz (without gz)
        //PRC012.A0101_S0_L001_R1_001.fastq.gz
        String basename = getOutputfolder()+output_stem;

        String zcatcombine = "zcat " +
        basename + ".collapsed.gz " +
        basename+".collapsed.truncated.gz ";

        if (!this.communicator.isMerge_keep_only_merged()) {
            zcatcombine += basename+".singleton.truncated.gz ";
            if(this.communicator.getMerge_type().equals("PAIRED")) {
                zcatcombine += basename+".pair1.truncated.gz ";
                zcatcombine += basename+".pair2.truncated.gz ";
            }
        }
        zcatcombine += "| gzip > " + basename+ ".combined.fq.gz";


                String[] params = new String[]{"/bin/sh", "-c", zcatcombine};
        return params;
    }

    @Override
    public String getOutputfolder() {
        return this.communicator.getGUI_resultspath()+"/1-AdapClip/";
    }

}
