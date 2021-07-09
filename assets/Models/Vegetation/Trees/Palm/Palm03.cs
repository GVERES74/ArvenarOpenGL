
singleton TSShapeConstructor(Palm03Dae)
{
   baseShape = "./Palm03.dae";
};

function Palm03Dae::onLoad(%this)
{
   %this.addImposter("64", "8", "0", "0", "512", "1", "0");
}
