out vec4 fragColor;
in vec3 color;

void main (void)
{
	fragColor = vec4(abs(color),1.0);
}

