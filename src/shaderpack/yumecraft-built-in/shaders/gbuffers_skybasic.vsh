#version 330 compatibility

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

in vec3 vaPosition;

out vec4 glcolor;


void main() {
	gl_Position = ftransform();
	glcolor = gl_Color;
}