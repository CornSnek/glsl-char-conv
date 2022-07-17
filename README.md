# glsl-char-conv
Converts each line in a GLSL file to add \"quotes\" and \"\\n\" in between.
The purpose or programming this is to write GLSL into a (constexpr) const char* instead of external .vert/.frag files.
Args: 2 File names (input_file output_file) without backslashes (Add quotes if spaces are included).
Third argument is to add or remove the quotes from the input file to the output file. Use add/remove for the third argument.
Example: java -jar glsl_char_conv.jar vertex.vert output.txt add
