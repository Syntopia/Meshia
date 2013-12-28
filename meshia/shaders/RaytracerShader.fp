out vec4 fragColor;
in vec3 dir;
in vec3 eye;
out float gl_FragDepth;

#define PI  3.14159265358979323846264

vec2 spherical(vec3 d) {
	return vec2( acos(d.z), atan(d.y,d.x) );
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


int MaximumRaySteps = 250;
float MinimumDistance = 0.00001;

float DistanceEstimator(vec3 p) {
	vec3 pm = mod(p,vec3(1.0));

	p.xy = abs(p.xy);
	p-=vec3(0.002);

	float d = length(pm-vec3(0.5))-0.1;
	
	return min(d,length(p.xy)-0.0001);
}

float trace(vec3 from, vec3 direction) {
	float totalDistance = 0.0;
	int steps;
	for (steps=0; steps < MaximumRaySteps; steps++) {
		vec3 p = from + totalDistance * direction;
		float distance = DistanceEstimator(p);
		totalDistance += distance;
		if (distance < MinimumDistance) break;
	}
	return 1.0-float(steps)/float(MaximumRaySteps);
}


void main (void)
{
	gl_FragDepth = 0.99; 

	vec3 nDir=normalize(dir);

	float d = trace(eye,nDir);
	if (d>0.02) {
		fragColor = vec4(vec3(d),1.0);
		return;
	}

	fragColor = vec4(abs(dot(nDir, vec3(1,0,0))), abs(dot(nDir, vec3(0,1,0))), abs(dot(nDir, vec3(0,0,1))),1.0); ;

	vec2 longlat = spherical(nDir);
	if (mod(abs(longlat.x),0.1)<0.01 || mod(abs(longlat.y),0.1)<0.01) {
		fragColor = vec4(0.0,0.0,0.0,1.0);
		return;
	}
}

