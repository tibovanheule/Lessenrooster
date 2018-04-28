package timetable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;


/**
 * Class to output an custom error message to the standard error
 */
public class StdError {
    /**
     * Function to output students,teachers or locations onto the standaard output
     */
    public StdError(String error) {
        try (BufferedWriter errorWriter = new BufferedWriter(new OutputStreamWriter(System.err))) {
            errorWriter.append(error);
            /*errorWriter.write(error);*/
            errorWriter.flush();

        } catch (IOException errorWhileGivingAErrorLol) {
            errorWhileGivingAErrorLol.printStackTrace();
        }
    }
}
