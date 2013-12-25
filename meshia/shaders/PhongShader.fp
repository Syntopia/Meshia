out vec4 fragColor;
in vec4 frontColor;
in vec4 rotatedNormal;

void main (void)
{
    fragColor = frontColor*0.2;
    float specular = vec3( clamp(dot(rotatedNormal.xyz,vec3(1,0,1)),0,1) );
    float diffuse = vec3(  clamp(dot(rotatedNormal.xyz,vec3(-1,1,1)),0,1) ,0,0);
    fragColor.xyz += vec3(0.2)+specular+diffuse;
//    fragColor = frontColor;
}

