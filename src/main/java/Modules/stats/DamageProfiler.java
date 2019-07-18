package Modules.stats;

import IO.Communicator;
import Modules.AModule;
import com.google.common.io.Files;

import java.util.Map;

/**
 * Created by neukamm on 12.09.16.
 */


public class DamageProfiler extends AModule {
    public DamageProfiler(Communicator c) {
        super(c);
    }

    @Override
    public void setProcessEnvironment (Map<String, String> env) {
        if ( !this.communicator.isUsesystemtmpdir() ) {
            AModule.setEnvironmentForParameterReplace (env,
                    "TMPDIR",
                    getOutputfolder() + System.getProperty ("file.separator") + ".tmp");
        }
        AModule.setEnvironmentForParameterDelete (env, "DISPLAY");
    }

    @Override
    public void setParameters() {
        String output_stem = Files.getNameWithoutExtension(this.getInputfile().get(0));
        String output_path = getOutputfolder();
        this.parameters = new String[]{"damageprofiler", "-i", this.getInputfile().get(0), "-r", this.communicator.getGUI_reference(),
                "-l", this.communicator.getDamageProfiler_length(),
                "-o", output_path+"/"+output_stem,
                "-t", this.communicator.getDamageProfiler_threshold(),
                this.communicator.getDamageProfiler_advanced()};
        this.outputfile = this.inputfile;
    }

    @Override
    public String getOutputfolder() {
        return this.communicator.getGUI_resultspath() + "/7-DnaDamage";
    }


}
