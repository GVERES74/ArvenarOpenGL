
singleton TSShapeConstructor(SeaPlant_01Dae)
{
   baseShape = "./SeaPlant_01.dae";
};

function SeaPlant_01Dae::onLoad(%this)
{
   %this.addImposter("64", "6", "0", "0", "512", "1", "0");
}
