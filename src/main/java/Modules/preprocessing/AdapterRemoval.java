package Modules.preprocessing;

import IO.Communicator;
import Modules.AModule;
import com.google.common.io.Files;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by peltzer on 19.04.17.
 */
public class AdapterRemoval extends AModule {

    public static final int DEFAULT = 0;
    private int currentConfiguration = DEFAULT;
    public static final int ADAPTER_CLIPPING_ONLY = 1;
    public static final int SINGLE_ENDED_ONLY = 2;

    public AdapterRemoval(Communicator c) { super(c);}

    public AdapterRemoval(Communicator c, int config){
        super(c);
        this.currentConfiguration = config;
    }


    @Override
    public void setParameters() {
        String output_stem = Files.getNameWithoutExtension(this.inputfile.get(0));
        this.outputfile = new ArrayList<String>();
        switch (currentConfiguration){
            case DEFAULT: this.parameters = getDefaultParameterList();
                break;
            case ADAPTER_CLIPPING_ONLY: this.parameters = getAdapterClippingOnlyParameterList();
                break;
            case SINGLE_ENDED_ONLY: this.parameters = getSingleEndedOnlyParameterList();
                break;
        }
    }


    private String[] getDefaultParameterList(){
        String output_stem = Files.getNameWithoutExtension(this.inputfile.get(0));
        String ext = Files.getFileExtension(this.inputfile.get(0));
        this.outputfile.add(getOutputfolder()+output_stem +"." + ext);

        List<String> cmd = new ArrayList<String>();
        cmd.add("AdapterRemoval");
        cmd.add("--file1");

        for (String s : getForwardFilePaths()) {
            cmd.add(s);
        }

        cmd.add("--file2");

        for (String s : getReverseFilePaths()) {
            cmd.add(s);
        }
        cmd.add("--basename");
        cmd.add(getOutputfolder()+ output_stem);
        cmd.add("--gzip");
        cmd.add("--threads");
        cmd.add(this.communicator.getCpucores());
        cmd.add("--trimns");
        cmd.add("--trimqualities");
        cmd.add("--preserve5p");
        cmd.add("--adapter1");
        cmd.add(this.communicator.getMerge_fwadaptor());
        cmd.add("--adapter2");
        cmd.add(this.communicator.getMerge_bwadaptor());
        cmd.add("--minlength");
        cmd.add(String.valueOf(this.communicator.getQuality_readlength()));
        cmd.add("--minquality");
        cmd.add(String.valueOf(this.communicator.getQuality_minreadquality()));
        cmd.add("--minadapteroverlap");
        cmd.add(String.valueOf(this.communicator.getMerge_min_adapter_overlap()));
        cmd.add("--collapse");

        if (this.communicator.isQualityBase64()) {
          cmd.add("--qualitybase");
          cmd.add("64");
          cmd.add("--qualitybase-output");
          cmd.add("33");
        }

        return cmd.toArray(new String[0]);
    }

    private String[] getAdapterClippingOnlyParameterList(){
        String output_stem = Files.getNameWithoutExtension(this.inputfile.get(0));

        this.outputfile.add(getOutputfolder()+output_stem+".pair1.truncated.gz");
        this.outputfile.add(getOutputfolder()+output_stem+".pair2.truncated.gz");

        List<String> cmd = new ArrayList<String>();
        cmd.add("AdapterRemoval");
        cmd.add("--file1");

        for (String s : getForwardFilePaths()) {
            cmd.add(s);
        }

        cmd.add("--file2");

        for (String s : getReverseFilePaths()) {
            cmd.add(s);
        }

        cmd.add("--basename");
        cmd.add(getOutputfolder()+ output_stem);
        cmd.add("--gzip");
        cmd.add("--threads");
        cmd.add(this.communicator.getCpucores());
        cmd.add("--trimns");
        cmd.add("--trimqualities");
        cmd.add("--preserve5p");
        cmd.add("--adapter1");
        cmd.add(this.communicator.getMerge_fwadaptor());
        cmd.add("--adapter2");
        cmd.add(this.communicator.getMerge_bwadaptor());
        cmd.add("--minlength");
        cmd.add(String.valueOf(this.communicator.getQuality_readlength()));
        cmd.add("--minquality");
        cmd.add(String.valueOf(this.communicator.getQuality_minreadquality()));
        cmd.add("--minadapteroverlap");
        cmd.add(String.valueOf(this.communicator.getMerge_min_adapter_overlap()));
        cmd.add("--combined-output");

        if (this.communicator.isQualityBase64()) {
          cmd.add("--qualitybase");
          cmd.add("64");
          cmd.add("--qualitybase-output");
          cmd.add("33");
        }

        return cmd.toArray(new String[0]);
    }

    private String[] getSingleEndedOnlyParameterList() {
        String output_stem = Files.getNameWithoutExtension(this.inputfile.get(0));
        this.outputfile.add(getOutputfolder()+output_stem+".truncated.gz");

        List<String> cmd = new ArrayList<String>();
        cmd.add("AdapterRemoval");
        cmd.add("--file1");

        for ( String s : getAllFilePaths() ) {
            cmd.add(s);
        }

        cmd.add("--basename");
        cmd.add(getOutputfolder()+ output_stem);
        cmd.add("--gzip");
        cmd.add("--threads");
        cmd.add(this.communicator.getCpucores());
        cmd.add("--trimns");
        cmd.add("--trimqualities");
        cmd.add("--preserve5p");
        cmd.add("--adapter1");
        cmd.add(this.communicator.getMerge_fwadaptor());
        cmd.add("--adapter2");
        cmd.add(this.communicator.getMerge_bwadaptor());
        cmd.add("--minlength");
        cmd.add(String.valueOf(this.communicator.getQuality_readlength()));
        cmd.add("--minquality");
        cmd.add(String.valueOf(this.communicator.getQuality_minreadquality()));
        cmd.add("--minadapteroverlap");
        cmd.add(String.valueOf(this.communicator.getMerge_min_adapter_overlap()));

        if (this.communicator.isQualityBase64()) {
          cmd.add("--qualitybase");
          cmd.add("64");
          cmd.add("--qualitybase-output");
          cmd.add("33");
        }

        return cmd.toArray(new String[0]);

    }


    @Override
    public String getOutputfolder() {
        return  this.communicator.getGUI_resultspath()+"/1-AdapClip/";
    }

    @Override
    public String getModulename(){
        return super.getModulename() + getSubModuleName();
    }

    public String getSubModuleName(){
        switch(currentConfiguration){
            case DEFAULT: return "default";
            case ADAPTER_CLIPPING_ONLY: return "adapterclippingonly";
            case SINGLE_ENDED_ONLY: return "singleendedonly";
            default: return "default";
        }

    }




    private List<String> getForwardFilePaths(){

        List<String> result = new ArrayList<String>(this.communicator.getGUI_inputfiles().size());
        for(String s : this.communicator.getGUI_inputfiles()){
            if(s.contains("R1_") || s.contains("R1.")){
                result.add(s);
            }
        }
        return result;
    }

    private List<String> getReverseFilePaths(){
        List<String> result = new ArrayList<String>(this.communicator.getGUI_inputfiles().size());
        for(String s : this.communicator.getGUI_inputfiles()){
            if(s.contains("R2_") || s.contains("R2.")){
                result.add(s);
            }
        }
        return result;
    }

    private List<String> getAllFilePaths(){
        List<String> result = new ArrayList<String>(this.communicator.getGUI_inputfiles().size());
        for (String s : this.communicator.getGUI_inputfiles()){
            result.add(s);
        }
        return result;
    }


}
