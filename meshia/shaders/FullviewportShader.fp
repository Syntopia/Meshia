out vec4 fragColor;
in vec3 dir;
in vec3 eye;
out float gl_FragDepth;
in mat4 transform;

#define PI  3.14159265358979323846264

vec2 spherical(vec3 dir) {
	return vec2( acos(dir.z), atan(dir.y,dir.x) );
}

// Hue in radians
vec3 HSVtoRGB(vec3 hsv) {
	/// Implementation based on: http://en.wikipedia.org/wiki/HSV_color_space
	hsv.x = mod(hsv.x,2.*PI);
	int Hi = int(mod(hsv.x / (2.*PI/6.), 6.));
	float f = (hsv.x / (2.*PI/6.)) -float( Hi);
	float p = hsv.z*(1.-hsv.y);
	float q = hsv.z*(1.-f*hsv.y);
	float t = hsv.z*(1.-(1.-f)*hsv.y);
	if (Hi == 0) { return vec3(hsv.z,t,p); }
	if (Hi == 1) { return vec3(q,hsv.z,p); }
	if (Hi == 2) { return vec3(p,hsv.z,t); }
	if (Hi == 3) { return vec3(p,q,hsv.z); }
	if (Hi == 4) { return vec3(t,p,hsv.z); }
	if (Hi == 5) { return vec3(hsv.z,p,q); }
	return vec3(0.);
}

float fov2scale(float fov){return tan(radians(fov/2.0));}


void main (void)
{
    gl_FragDepth = 0.99; // 1.0-abs(dir.y);

 vec3 dirx=vec3(transform*vec4(dir.x*fov2scale(45),dir.y*fov2scale(45),-1.0,0));
  dirx = normalize(dirx);
 

fragColor = vec4(abs(dirx),1.0);

    fragColor = vec4(abs(dot(dirx, vec3(1,0,0))), abs(dot(dirx, vec3(0,1,0))), abs(dot(dirx, vec3(0,0,1))),1.0); ;
  //  return;
    
	vec2 longlat = spherical(dirx);
if (mod(abs(longlat.x),0.1)<0.01) {
 fragColor = vec4(0.0,0.0,0.0,1.0);
  return;
}
if (mod(abs(longlat.y),0.1)<0.01) {
 fragColor = vec4(1.0,1.0,1.0,1.0);
  return;
}


//    fragColor = vec4(vec3(dot(dir,vec3(1.0,0.0,0.0))),1.0);
    //  fragColor = vec4(HSVtoRGB(vec3(longlat.x, 1.0, longlat.y/PI)),1.0);
//      fragColor = vec4(abs(normalize(dir)),1.0);
  
}

