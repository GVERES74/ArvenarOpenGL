
singleton Material(QueensPalm01_PalmMapped)
{
   mapTo = "PalmMapped";
   diffuseMap[0] = "PalmTrunkMap01";
   specular[0] = "0.9 0.9 0.9 1";
   specularPower[0] = "10";
   translucentBlendOp = "None";
   normalMap[0] = "PalmTrunkMap01_NRM";
   useAnisotropic[0] = "1";
   doubleSided = "1";
};

singleton Material(QueensPalm01_QueensPalm)
{
   mapTo = "QueensPalm";
   diffuseMap[0] = "QueensPalm01";
   specular[0] = "0.9 0.9 0.9 1";
   specularPower[0] = "24";
   translucentBlendOp = "None";
   useAnisotropic[0] = "1";
   subSurface[0] = "1";
   subSurfaceColor[0] = "0.733333 0.823529 0.34902 1";
   doubleSided = "1";
   alphaTest = "1";
   alphaRef = "120";
   specularMap[0] = "QueensPalm01_SPEC";
   diffuseColor[0] = "0.996078 0.996078 0.992157 1";
   normalMap[0] = "QueensPalm01_NRM";
};

singleton Material(QueensPalm01_ColorEffectR225G88B199_material)
{
   mapTo = "ColorEffectR225G88B199-material";
   diffuseColor[0] = "0.882353 0.345098 0.780392 1";
   specularPower[0] = "10";
   translucentBlendOp = "None";
};
