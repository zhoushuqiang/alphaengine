attribute vec4 a_Position;
attribute vec2 a_TexCoordinate;

varying vec2 v_TexCoordinate;

void main()
{
    gl_Position = a_Position;
    v_TexCoordinate = a_TexCoordinate;
}