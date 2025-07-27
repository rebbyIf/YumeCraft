#version 330 compatibility

#define PI 3.1415926538

uniform int renderStage;
uniform float viewHeight;
uniform float viewWidth;
uniform mat4 gbufferModelView;
uniform mat4 gbufferProjection;
uniform mat4 gbufferModelViewInverse;
uniform mat4 gbufferProjectionInverse;
uniform vec3 fogColor;
uniform vec3 skyColor;
uniform sampler2D skybox1;
uniform sampler2D wispy_skybox;

in vec4 glcolor;

float fogify(float x, float w) {
	return w / (x * x + w);
}

vec3 calcSkyColor(vec3 pos) {
	float upDot = dot(pos, gbufferModelView[1].xyz); //not much, what's up with you?

	vec3 a = (vec4(pos,0) * gbufferModelView).xyz;
	float m = max(abs(a.x),max(abs(a.y),abs(a.z)));
	vec3 b = a / m;
	vec3 c = b * vec3(0.5,-0.5,0.5) + vec3(0.5,-0.5,0.5);
	vec2 d = vec2(0,0);
	if (c.z >= -0.001 && c.z < 0.001) {
		d = c.xy / vec2(-3,2) + vec2(4.0/3, 0);
	} else if (c.z > 0.9999 && c.z <= 1.0001) {
		d = c.xy / vec2(3,2) + vec2(2.0/3, 0);
	} else if (c.x >= -0.0001 && c.x < 0.0001) {
		d = c.zy / vec2(3,2) + vec2(1.0/3, 0);
	} else if (c.x > 0.9999 && c.x <= 1.0001) {
		d = c.zy / vec2(-3,2) + vec2(6.0/3, 1.0/2);
	} else if (c.y >= -0.0001 && c.y < 0.0001) {
		d = c.zx / vec2(3,2) + vec2(0, 0);
	} else if (c.y < -0.9999 && c.y >= -1.0001){
		d = c.zx / vec2(3,-2) + vec2(1.0/3, 3.0/2);
	}
	//vec2 cartesian = vec2(atan(sqrt(a.x * a.x + a.z * a.z), a.y), atan(a.z/a.x));

	vec3 color = texture(wispy_skybox, d).xyz;

	vec3 before = mix(color, fogColor, fogify(max(upDot, 0.0), 0.25));


	return color;
	//return before;
}

vec3 screenToView(vec3 screenPos) {
	vec4 ndcPos = vec4(screenPos, 1.0) * 2.0 - 1.0;
	vec4 tmp = gbufferProjectionInverse * ndcPos;
	return tmp.xyz / tmp.w;
	//return screenPos;
}

/* RENDERTARGETS: 0 */
layout(location = 0) out vec4 color;

void main() {
	vec3 pos = screenToView(vec3(gl_FragCoord.xy / vec2(viewWidth, viewHeight), 1.0));
	color = vec4(calcSkyColor(normalize(pos)), 1.0);
}