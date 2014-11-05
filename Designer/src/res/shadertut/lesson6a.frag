//attributes from vertex shader
varying vec4 vColor;
varying vec2 vTexCoord;

//our texture samplers
uniform sampler2D u_texture;   //diffuse map
uniform sampler2D u_normals;   //normal map

//values used for shading algorithm...
uniform vec2 Resolution;      //resolution of screen
uniform vec3 LightPos;        //light position, normalized
uniform vec4 LightColor;      //light RGBA -- alpha is intensity
uniform vec4 AmbientColor;    //ambient RGBA -- alpha is intensity 
uniform vec3 Attenuation;     //attenuation coefficients

void main() {
	//RGBA of our diffuse color
	vec4 DiffuseColor = texture2D(u_texture, vTexCoord);
	
	//RGB of our normal map
	vec3 NormalMap = texture2D(u_normals, vTexCoord).rgb;
	
	//The delta position of light
	vec3 LightDir = vec3(LightPos.xy - (gl_FragCoord.xy / Resolution.xy), LightPos.z);
		
	//normalize our vectors
	vec3 N = normalize(NormalMap * 2.0 - 1.0);
	vec3 L = normalize(LightDir);
	
	//Then perform "N dot L" to determine our diffuse term
	vec3 Diffuse = vec3(1.0) * max(dot(N, L), 0.0);

	//the calculation which brings it all together
	vec3 Intensity = Diffuse;
	
	vec3 FinalColor = DiffuseColor.rgb * Intensity;
	
	gl_FragColor = vColor * vec4(FinalColor, DiffuseColor.a);
}