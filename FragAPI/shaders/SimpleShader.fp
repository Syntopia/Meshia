out vec4 fragColor;
out float gl_FragDepth;

in vec2 pos;

uniform sampler2D hdrBuffer;
uniform sampler2D image;

void main (void)
{
	gl_FragDepth = 0.99; 
	vec4 col = texture2D(hdrBuffer,pos);
  	vec4 col2 = texture2D(image,pos);
    col.xyz *= col2.xyz;
	fragColor = vec4(col.xyz,1.0);	
}

