package net.year4000.utilities.locale;

import net.year4000.utilities.LogUtil;
import lombok.NoArgsConstructor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileInputStream;

import static com.google.common.base.Preconditions.checkArgument;

@NoArgsConstructor
@SuppressWarnings("unused")
public class FileLocaleManager extends AbstractLocaleManager {
    /**
     * Load the locales provided and use a File object as a base location.
     * @param file The file object to use as a base location.
     * @param locales The locales to use by default
     */
    @ParametersAreNonnullByDefault
    public FileLocaleManager(File file, String... locales) {
        this(isFileDirectory(file).toPath().toString() + "/", locales);
    }

    /**
     * Load the locales provided and use a File object as a base location.
     * @param path The file path to use when getting the files.
     * @param locales The locales to use by default
     */
    @ParametersAreNonnullByDefault
    public FileLocaleManager(String path, String... locales) {
        super(path, locales);
    }

    /**
     * Load the locales provided and use a File object as a base location.
     * @param log The LogUtil to use when creating this locale manager.
     * @param path The file path to use when getting the files.
     * @param locales The locales to use by default.
     */
    @ParametersAreNonnullByDefault
    public FileLocaleManager(LogUtil log, String path, String... locales) {
        super(path, locales, log);
    }

    /** Load all the locales that are in the folder */
    public void loadLocales() {
        for (String code : codes) {
            try {
                File file = new File(path, code + EXTENSION);

                loadLocale(code, new FileInputStream(file));
            }
            catch (Exception e) {
                //log.log(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /** Check is the argument is a file and an a directory, internal usage. */
    private static <T> T isFileDirectory(T file) {
        checkArgument(file instanceof File && ((File) file).isDirectory());
        return file;
    }
}
