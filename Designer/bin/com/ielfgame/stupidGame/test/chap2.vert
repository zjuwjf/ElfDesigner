#version 140
 
in vec4 in_Position;
in vec4 in_Color;
out vec4 ex_Color;
 
void main(void)
{
   gl_Position = in_Position;
   ex_Color = in_Color;
}