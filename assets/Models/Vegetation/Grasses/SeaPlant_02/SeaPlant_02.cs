
singleton TSShapeConstructor(SeaPlant_02DAE)
{
   baseShape = "./SeaPlant_02.DAE";
};

function SeaPlant_02DAE::onLoad(%this)
{
   %this.addImposter("64", "6", "0", "0", "512", "1", "0");
}
