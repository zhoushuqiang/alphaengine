precision mediump float;

uniform sampler2D u_Texture;
uniform sampler2D u_TextureAlpha;
varying vec2 v_TexCoordinate;

void main()
{
    vec4 colour = texture2D(u_Texture, v_TexCoordinate);
    colour.a = texture2D(u_TextureAlpha, v_TexCoordinate).r;
    gl_FragColor = colour;
}