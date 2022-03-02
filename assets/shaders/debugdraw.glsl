#type vertex
#version 330 core
layout (location=0) in vec3 aPos;

uniform mat4 uProjection;
uniform mat4 uView;

void main()
{
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

out vec4 color;

void main()
{
    color = vec4(0,0,0,1);
}
