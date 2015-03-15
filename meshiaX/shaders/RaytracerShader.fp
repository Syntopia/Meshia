out vec4 fragColor;
out float gl_FragDepth;

in vec3 dir;
in vec3 cameraForward;
in vec3 eye;

uniform vec2 farNear;

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


int MaximumRaySteps = 50;
float MinimumDistance = 0.001;

float DistanceEstimator(vec3 p) {
	float d = 1000.0; // length(pm-vec3(0.5))-0.1;
	d = 1000.0;
	d = min(d,max(p.y,abs(length(p.xz))-0.1));
	
	d = min(d,abs(length(p.yz))-0.1);

	d = min(d,abs(length(p.xy))-0.1);
return d;
}


float DistanceEstimator2(vec3 p) {
	vec3 pm = mod(p,vec3(10.0));
	p = pm;
	float d = 1000.0; // length(pm-vec3(0.5))-0.1;
	d = 1000.0;
	d = min(d,max(p.y,abs(length(p.xz-vec2(5.0)))-0.1));
	d = min(d,abs(length(p.yz-vec2(5.0)))-0.1);
	d = min(d,abs(length(p.xy-vec2(5.0)))-0.1);
	return d;
}



vec2 trace(vec3 from, vec3 direction) {
	float totalDistance = 0.0;
	int steps;
	for (steps=0; steps < MaximumRaySteps; steps++) {
		vec3 p = from + totalDistance * direction;
		float distance = DistanceEstimator(p);
		totalDistance += distance;
		if (distance < MinimumDistance) break;
	}
	return vec2(1.0-float(steps)/float(MaximumRaySteps), totalDistance);
}


void main (void)
{
	gl_FragDepth = 0.99; 
	
	vec3 rayDirection=normalize(dir);
	
	vec2 t = trace(eye,rayDirection);
	if (t.x>0.00) {
		fragColor = vec4(vec3(t.x),1.0);
		float zFar = farNear[0];
		float zNear = farNear[1];
		float eyeHitZ = -t.y *dot(normalize(cameraForward),rayDirection);
		float ndcDepth = ((zFar+zNear) + (2.0*zFar*zNear)/eyeHitZ)/(zFar-zNear);
		gl_FragDepth =((gl_DepthRange.diff * ndcDepth) + gl_DepthRange.near + gl_DepthRange.far) / 2.0;
		return;
	}

	fragColor = vec4(abs(dot(rayDirection, vec3(1,0,0))), abs(dot(rayDirection, vec3(0,1,0))), abs(dot(rayDirection, vec3(0,0,1))),1.0); ;
	vec2 longlat = spherical(rayDirection.yzx);
	if (mod(abs(longlat.x),0.1)<0.01 || mod(abs(longlat.y),0.1)<0.01) {
		fragColor = vec4(0.0,0.0,0.0,1.0);
		return;
	}
}

