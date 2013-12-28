out vec4 fragColor;
in vec4 frontColor;
in vec4 rotatedNormal;


vec3 getLighting(vec3 color, vec3 lightDir, vec3 normal) {
    float lambertTerm = dot(normal,lightDir);

    if(lambertTerm > 0.0)
    {
    vec3 eyeVec= vec3(1.0,0.0,0.0);
        vec3 E = normalize(eyeVec);
        vec3 R = reflect(-lightDir, normal);
        float specular = pow( max(dot(R, E), 0.0), 50.0);
        return color * vec3(0.0,1.0,0.0) * lambertTerm +  vec3(1.0,0.0,0.0) * specular;
    }
    return vec3(0);
}

void main (void)
{
    vec3 ambient = frontColor.xyz*0.2; 
    fragColor.xyz = ambient+ getLighting(frontColor.xyz, vec3(0.0,1.0,1.0), rotatedNormal.xyz);
}

