out vec4 fragColor;
out float gl_FragDepth;

in vec2 pos;

uniform sampler2D bufferx;

void main (void)
{
	gl_FragDepth = 0.99; 
	vec4 col = texture2D(bufferx,pos);
    col.xy += pos.xy;
	fragColor = vec4(col.xyz,1.0);	
}

