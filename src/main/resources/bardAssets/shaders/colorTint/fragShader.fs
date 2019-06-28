//SpriteBatch will use texture unit 0
uniform sampler2D u_texture;
uniform vec4 tint;

//"in" varyings from our vertex shader
varying vec2 v_texCoord;

void main() {
    //sample the texture
    vec4 texColor = texture2D(u_texture, v_texCoord);
    //final color
    gl_FragColor = texColor * tint;
}
