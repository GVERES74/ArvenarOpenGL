
singleton TSShapeConstructor(CiabaTree_01Dae)
{
   baseShape = "./CiabaTree_01.dae";
   loadLights = "0";
};

function CiabaTree_01Dae::onLoad(%this)
{
   %this.addImposter("0", "8", "0", "0", "512", "1", "0");
}
