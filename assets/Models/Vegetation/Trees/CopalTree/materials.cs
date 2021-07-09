
singleton Material(CopalTree01_Fishtail)
{
   mapTo = "Fishtail";
   diffuseMap[0] = "FishtailFrond";
   specular[0] = "0.9 0.9 0.9 1";
   specularPower[0] = "10";
   translucentBlendOp = "None";
   specularMap[0] = "FishtailFrond_SPEC.dds";
   useAnisotropic[0] = "1";
   subSurface[0] = "1";
   subSurfaceColor[0] = "0.733333 0.917647 0.423529 1";
   doubleSided = "1";
   alphaTest = "1";
   alphaRef = "100";
   subSurfaceRolloff[0] = "0.4";
};

singleton Material(CopalTree01_Vino)
{
   mapTo = "Vino";
   diffuseMap[0] = "Vines02.dds";
   specular[0] = "0.9 0.9 0.9 1";
   specularPower[0] = "10";
   translucentBlendOp = "None";
   doubleSided = "1";
   alphaTest = "1";
   alphaRef = "120";
   useAnisotropic[0] = "1";
   normalMap[0] = "Vines02_NRM.dds";
};


singleton Material(CopalTree01_ColorEffectR166G229B229_material)
{
   mapTo = "ColorEffectR166G229B229-material";
   diffuseColor[0] = "0.65098 0.898039 0.898039 1";
   specularPower[0] = "10";
   translucentBlendOp = "None";
};

singleton Material(CopalTree01_JungleBark02)
{
   mapTo = "JungleBark02";
   diffuseMap[0] = "JungleBark02";
   specular[0] = "0.9 0.9 0.9 1";
   specularPower[0] = "21";
   translucentBlendOp = "None";
   normalMap[0] = "JungleBark02_NRM.dds";
   specularMap[0] = "JungleBark02_SPEC.dds";
   useAnisotropic[0] = "1";
   doubleSided = "1";
};
