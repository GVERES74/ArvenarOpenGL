
singleton Material(TropicFern01)
{
   mapTo = "TropicFern01";
   diffuseMap[0] = "fern_image2.png";
   normalMap[0] = "fern_image2_NRM.png";
   specular[0] = "0.9 0.9 0.9 1";
   specularPower[0] = "25";
   specularMap[0] = "fern_image2_SPEC.png";
   subSurface[0] = "1";
   subSurfaceColor[0] = "0.643137 0.815686 0.321569 1";
   doubleSided = "1";
   translucentBlendOp = "None";
   alphaTest = "1";
   alphaRef = "60";
   diffuseColor[0] = "1 0.858824 0 1";
};

singleton Material(TropicFern01_FernSprout01)
{
   mapTo = "FernSprout01";
   diffuseMap[0] = "ferntop.png";
   specular[0] = "0.9 0.9 0.9 1";
   specularPower[0] = "10";
   useAnisotropic[0] = "1";
   doubleSided = "1";
   translucentBlendOp = "None";
   alphaTest = "1";
   alphaRef = "60";
   diffuseColor[0] = "1 0.882353 0 1";
};
