out vec4 fragColor;
in vec4 frontColor;
in vec3 rotatedNormal;
in vec3 worldNormal;

vec2 spherical(vec3 d) {
	return vec2( acos(d.z), atan(d.y,d.x) );
}

vec3 getReflectColor(vec3 rayDirection) {
   vec2 longlat = spherical(rayDirection.yzx);
   if (mod(abs(longlat.x),0.1)<0.01 || mod(abs(longlat.y),0.1)<0.01) {
	  return vec3(0.0,0.0,0.0);
   }
   return vec3(abs(dot(rayDirection, vec3(1,0,0))), abs(dot(rayDirection, vec3(0,1,0))), abs(dot(rayDirection, vec3(0,0,1)))); 

}

vec3 getLighting(vec3 color, vec3 lightDir, vec3 normal) {
	float lambertTerm = dot(normalize(normal),normalize(lightDir));
//	return getReflectColor(-worldNormal);
	if(lambertTerm > 0.0)
	{
		vec3 eyeVec= vec3(1.0,0.0,0.0);
		vec3 E = normalize(eyeVec);
		vec3 R = reflect(-lightDir, normal);
		float specular = 0.1*pow( clamp(dot(R, E), 0.0,1.0), 150.0);
		return 0.6*color * vec3(1.0,0.8,0.8) * lambertTerm +  vec3(1.0,1.0,1.0) * specular;
		//	+
		//		0.3* getReflectColor(worldNormal);
	}
	return vec3(0);
}

void main (void)
{
	vec3 ld =  vec3(0.0,1.0,1.0);
	if (dot(rotatedNormal, ld)>0) {
		vec3 ambient = frontColor.xyz*0.2; 
		fragColor.xyz = ambient+ getLighting(frontColor.xyz,ld, rotatedNormal);
	} else {
		// backside
		vec3 c = frontColor.xyz; //vec3(0.0,0.6,0.1);
		vec3 ambient = c*0.2; 
			
		fragColor.xyz = ambient+ getLighting(c, ld, -rotatedNormal);
     }
}

