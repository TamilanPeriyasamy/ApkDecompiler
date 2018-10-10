# ApkDecompiler
 This tool decode apk resources and rebuild them after making some modifications.
 
### USAGE: ApkDecompiler < command > [options]

    ApkDecompiler decompile
    ApkDecompiler build

### EXAMPLE:

    ApkDecompiler decompile -in app.apk -out app-dir
    ApkDecompiler build -in app-dir -out app.apk 

    *ApkDecompiler is a tool for reverse engineering Android APK files.

### COMMANDS

    d or decompile       Decompile the provided APK resources

    b or build           Build decompiled APK resources files

    -in                  Input file or Input Directory

    -out                 Output file or Output Directory

    -sign                Sign the provided APK (use a test certificate)

    -help                Show this usage page and exit
