#pragma version(1)
#pragma rs java_package_name(edu.gordon.cs.renderscriptexample)

// Renderscript Kernel - This is executed on each element
uchar4 __attribute__((kernel)) slowlyChange(const uchar4 in, uint32_t x, uint32_t y) {
    uchar4 out = {(uchar) 0, (uchar) 0, (uchar) 0, (uchar) 255};
    float average = (in.x + in.y + in.z) / 3;
    out.x = in.x - (in.x - average)/1000;
    out.y = in.y - (in.y - average)/1000;
    out.z = in.z - (in.z - average)/1000;
    return out;
}