
singleton Material(waterReeds_01_dae_ReedGrass_01)
{
   mapTo = "ReedGrass_01";
   diffuseMap[0] = "3td_ReedGrass_01";
   specular[0] = "0.9 0.9 0.9 1";
   specularPower[0] = "10";
   translucentBlendOp = "None";
};

singleton Material(waterReeds_01_ReedGrass_02)
{
   mapTo = "ReedGrass_02";
   diffuseMap[0] = "3td_ReedGrass_02";
   specular[0] = "0.9 0.9 0.9 1";
   specularPower[0] = "10";
   translucentBlendOp = "None";
   useAnisotropic[0] = "1";
   subSurface[0] = "1";
   subSurfaceColor[0] = "0.576471 0.623529 0.203922 1";
   doubleSided = "1";
   alphaTest = "1";
   alphaRef = "100";
   normalMap[0] = "3td_ReedGrass_02_NRM.png";
   specularMap[0] = "3td_ReedGrass_02_SPEC.png";
};
